use serde::de::DeserializeOwned;
use tauri::{fs, plugin::PluginApi, AppHandle, Manager, Runtime};

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
}
