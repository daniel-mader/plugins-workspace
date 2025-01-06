import SwiftRs
import Tauri
import UIKit
import WebKit

class PingArgs: Decodable {
  let value: String?
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
}

@_cdecl("init_plugin_cloud_storage")
func initPlugin() -> Plugin {
  return CloudStoragePlugin()
}
