use std::path::{Path, PathBuf};

use tauri::plugin::PermissionState;
use tauri::{command, AppHandle, Runtime};

use crate::models::*;
use crate::CloudStorageExt;
use crate::Result;

#[command]
pub(crate) async fn check_permissions<R: Runtime>(
    app: AppHandle<R>,
) -> Result<PermissionStateValue> {
    app.cloud_storage().check_permissions()
}

// #[command]
// pub(crate) async fn write_data<R: Runtime>(app: AppHandle<R>, args: WriteArgs) -> Result<()> {
//     app.cloud_storage().write_data(args)
// }

// #[command]
// pub(crate) async fn list_files<R: Runtime>(app: AppHandle<R>, args: ListArgs) -> Result<Vec<FileAttributes>> {
//     app.cloud_storage().list_files(args)
// }

// #[command]
// pub(crate) async fn exists<R: Runtime>(
//     app: AppHandle<R>,
//     args: FileArgs,
// ) -> Result<FileAttributes> {
//     app.cloud_storage().exists(args)
// }

// #[command]
// pub(crate) async fn delete<R: Runtime>(app: AppHandle<R>, args: FileArgs) -> Result<String> {
//     app.cloud_storage().delete(args)
// }

#[cfg(mobile)]
#[command]
pub(crate) async fn get_dir<R: Runtime>(app: AppHandle<R>) -> Result<ProviderArgs> {
    app.cloud_storage().get_dir()
}
