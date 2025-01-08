use tauri::plugin::PermissionState;
use tauri::{command, AppHandle, Runtime};

use crate::models::*;
use crate::CloudStorageExt;
use crate::Result;

#[command]
pub(crate) async fn ping<R: Runtime>(
    app: AppHandle<R>,
    payload: PingRequest,
) -> Result<PingResponse> {
    app.cloud_storage().ping(payload)
}

#[command]
pub(crate) async fn check_permissions<R: Runtime>(app: AppHandle<R>) -> Result<String> {
    app.cloud_storage().check_permissions()
}

#[command]
pub(crate) async fn write<R: Runtime>(app: AppHandle<R>, value: String) -> Result<String> {
    // let write_data = WriteData::from(value);
    app.cloud_storage().write(WriteData { value })
}

#[command]
pub(crate) async fn exists<R: Runtime>(app: AppHandle<R>) -> Result<FileAttributes> {
    app.cloud_storage().exists()
}
