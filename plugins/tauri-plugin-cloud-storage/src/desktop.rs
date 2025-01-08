use serde::de::DeserializeOwned;
use tauri::{
    fs,
    plugin::{PermissionState, PluginApi},
    AppHandle, Manager, Runtime,
};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
    app: &AppHandle<R>,
    _api: PluginApi<R, C>,
) -> crate::Result<CloudStorage<R>> {
    Ok(CloudStorage(app.clone()))
}

/// Access to the cloud-storage APIs.
pub struct CloudStorage<R: Runtime>(AppHandle<R>);

impl<R: Runtime> CloudStorage<R> {
    pub fn ping(&self, payload: PingRequest) -> crate::Result<PingResponse> {
        Ok(PingResponse {
            value: payload.value,
        })
    }

    pub fn status(&self) -> crate::Result<Status> {
        Ok(Status {
            is_available: false,
            error: None,
        })
        // Err(crate::Error::Io(std::io::Error::new(
        //     std::io::ErrorKind::Unsupported,
        //     "Not implemented",
        // )))
    }

    pub fn check_permissions(&self) -> crate::Result<String> {
        // Ok(PermissionState::Denied)
        Ok("disabled".to_string())
    }

    pub fn write(&self, payload: WriteData) -> crate::Result<String> {
        println!("writing data: {:?}", payload);
        Ok("nop".to_string())
        // self.0
        //     .run_mobile_plugin("write", payload)
        //     .map_err(Into::into)
    }

    pub fn exists(&self) -> crate::Result<FileAttributes> {
        Ok(FileAttributes {
            size: 0,
            modification_date: "none".to_string(),
        })
    }

    // TODO: check iCloud access locally
    // pub fn check_permissions(&self) -> crate::Result<PermissionStatus> {
    //     Ok(PermissionStatus::default())
    // }
}
