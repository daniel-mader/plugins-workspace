use std::path::PathBuf;

use serde::{Deserialize, Serialize};
use tauri::plugin::PermissionState;

// #[derive(Debug, Clone, Deserialize)]
// #[serde(rename_all = "camelCase")]
// pub struct Status {
//     pub is_available: bool,
//     // pub biometry_type: BiometryType,
//     pub error: Option<String>,
//     // pub error_code: Option<String>,
// }

// #[derive(Debug, Deserialize, Serialize)]
// #[serde(rename_all = "camelCase")]
// pub struct WriteArgs {
//     #[cfg(not(target_os = "ios"))]
//     pub path_uri: String, // On iCloud, the user cannot choose the location
//     pub file_name: String,
//     pub data: Vec<u8>,
// }

// // #[cfg(not(target_os = "ios"))]
// #[derive(Debug, Deserialize, Serialize)]
// #[serde(rename_all = "camelCase")]
// pub struct FileArgs {
//     pub file_uri: String,
// }

// #[derive(Debug, Deserialize, Serialize)]
// #[serde(rename_all = "camelCase")]
// pub struct FileAttributes {
//     pub provider: String, // TODO: should providers be typed more strictly?
//     pub size: u64,
//     pub modification_date: String,
// }

// #[derive(Debug, Clone, Default, Deserialize, Serialize)]
// #[serde(rename_all = "camelCase")]
// pub struct WriteResponse {
//     pub value: String,
// }

// #[derive(Debug, Clone, Default, Deserialize, Serialize)]
// #[serde(rename_all = "camelCase")]
// pub struct StringValue {
//     pub value: String,
// }

/// Note: Since Android always needs to return an object, we need to wrap the value to have a consistent API.
#[derive(Debug, Clone, Default, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct PermissionStateValue {
    pub value: PermissionState,
}

#[derive(Debug, Clone, Default, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct ProviderArgs {
    pub alias: Option<String>, // "iCloud", "Google Drive", "Local filesystem"
    pub path: Option<String>,
}
