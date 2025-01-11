# Tauri Plugin: Cloud Storage

Read and write files to a cloud storage provider (e.g. iCloud, Google Drive).

| Platform | Supported |
| -------- | --------- |
| Linux    | x         |
| Windows  | x         |
| macOS    | x (\*)    |
| Android  | ✓         |
| iOS      | ✓         |

(\*) Files are persisted to the local app folder instead of the cloud. This allows for a smoother development experience while still using the same API.

## API

tbd

## Implementation Details

### iOS

The iOS implementation uses an [iCloud Documents](https://developer.apple.com/documentation/xcode/configuring-icloud-services/) container. User authentication is handled by iOS.

### Android

The Android implementation uses Android's native [Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider) where the actual "Document provider" is abstracted.

On first use, the user picks a location (`ACTION_OPEN_DOCUMENT_TREE`) which returns a URI that can be used in the background to write data.
