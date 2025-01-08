use std::io::Write;

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

const FILE_PATH: &str = "/Users/daniel/Library/Application Support/com.impierce.unime/backup.txt";

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
        let mut file = std::fs::File::create(FILE_PATH).unwrap();
        file.write_all(payload.value.as_bytes()).unwrap();
        Ok("done".to_string())
    }

    pub fn exists(&self) -> crate::Result<FileAttributes> {
        let metadata = std::fs::metadata(FILE_PATH).unwrap();

        let modified: chrono::DateTime<chrono::Utc> = metadata.modified().unwrap().into();

        Ok(FileAttributes {
            provider: "Local filesystem".to_string(),
            size: metadata.len(),
            modification_date: modified.to_rfc3339(),
        })
    }

    pub fn delete(&self) -> crate::Result<String> {
        std::fs::remove_file(FILE_PATH).unwrap();
        Ok("success".to_string())
    }

    // TODO: check iCloud access locally
    // pub fn check_permissions(&self) -> crate::Result<PermissionStatus> {
    //     Ok(PermissionStatus::default())
    // }
}
