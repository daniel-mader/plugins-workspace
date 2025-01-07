import SwiftRs
import Tauri
import UIKit
import WebKit

class PingArgs: Decodable {
  let value: String?
}

class WriteArgs: Decodable {
  let value: String
}

class CloudStoragePlugin: Plugin {
  @objc public func ping(_ invoke: Invoke) throws {
    let args = try invoke.parseArgs(PingArgs.self)
    invoke.resolve(["value": args.value ?? ""])
  }

  private func getPermissionState() -> String {
    var permissionState: String

    let fileManager = FileManager.default

    if let _ = fileManager.url(forUbiquityContainerIdentifier: nil) {
      permissionState = "enabled"
    } else {
      permissionState = "disabled"
    }

    // switch AVCaptureDevice.authorizationStatus(for: .video) {
    // case .authorized:
    //   permissionState = "granted"
    // case .denied:
    //   permissionState = "denied"
    // default:
    //   permissionState = "prompt"
    // }

    return permissionState
  }

  @objc override func checkPermissions(_ invoke: Invoke) {
    let permissionState = getPermissionState()
    invoke.resolve(["cloudStorage": permissionState])
  }

  private func iCloudDocumentsDirectory() -> URL? {
    let fileManager = FileManager.default
    // Pass nil or your container identifier if you've set one in entitlements
    guard let ubiquityURL = fileManager.url(forUbiquityContainerIdentifier: nil) else {
        print("iCloud not available or disabled.")
        return nil
    }

    // Typically store files in the "Documents" subfolder of your ubiquity container
    let documentsURL = ubiquityURL.appendingPathComponent("Documents")
    return documentsURL
}

  @objc public func write(_ invoke: Invoke) throws {
    let args = try invoke.parseArgs(WriteArgs.self)
    let result = args.value + "-cloud"
    invoke.resolve(["value": result])
      
    // 1. Get iCloud Documents directory URL
    guard let iCloudDocumentsURL = iCloudDocumentsDirectory() else {
        throw NSError(domain: "iCloud", code: 0, userInfo: [NSLocalizedDescriptionKey: "iCloud not available"])
    }

    // 2. Create the destination file URL
    let fileURL = iCloudDocumentsURL.appendingPathComponent("test.dat")

    // 3. Ensure the directory exists (Documents folder should already exist, but you can create subfolders if needed)
    let fileManager = FileManager.default
    if !fileManager.fileExists(atPath: iCloudDocumentsURL.path) {
        try fileManager.createDirectory(at: iCloudDocumentsURL, withIntermediateDirectories: true)
    }

    let rawBytes: [UInt8] = Array(args.value.utf8)
    let data = Data(rawBytes)

    // 4. Write data to file
    //    Using `.atomic` helps ensure partial writes won't corrupt the file.
    try data.write(to: fileURL, options: .atomic)
  }
}

@_cdecl("init_plugin_cloud_storage")
func initPlugin() -> Plugin {
  return CloudStoragePlugin()
}
