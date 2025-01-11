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
pub(crate) async fn check_permissions<R: Runtime>(app: AppHandle<R>) -> Result<StringValue> {
    app.cloud_storage().check_permissions()
}

#[command]
pub(crate) async fn write<R: Runtime>(app: AppHandle<R>, args: WriteArgs) -> Result<WriteResponse> {
    app.cloud_storage().write(args)
}

#[command]
pub(crate) async fn exists<R: Runtime>(
    app: AppHandle<R>,
    args: FileArgs,
) -> Result<FileAttributes> {
    app.cloud_storage().exists(args)
}

#[command]
pub(crate) async fn delete<R: Runtime>(app: AppHandle<R>, args: FileArgs) -> Result<String> {
    app.cloud_storage().delete(args)
}
