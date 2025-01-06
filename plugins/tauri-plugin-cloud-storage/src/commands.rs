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
pub(crate) async fn status<R: Runtime>(app: AppHandle<R>) -> Result<Status> {
    app.cloud_storage().status()
}
