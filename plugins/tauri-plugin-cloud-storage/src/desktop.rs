use std::io::Write;

use serde::de::DeserializeOwned;
use tauri::{
    fs,
    plugin::{PermissionState, PluginApi},
    AppHandle, Manager, Runtime,
};

use crate::{models::*, Error};

pub fn init<R: Runtime, C: DeserializeOwned>(
    app: &AppHandle<R>,
    _api: PluginApi<R, C>,
) -> crate::Result<CloudStorage<R>> {
    Ok(CloudStorage(app.clone()))
}

/// Access to the cloud-storage APIs.
pub struct CloudStorage<R: Runtime>(AppHandle<R>);

impl<R: Runtime> CloudStorage<R> {
    // pub fn status(&self) -> crate::Result<Status> {
    //     Ok(Status {
    //         is_available: false,
    //         error: None,
    //     })
    //     // Err(crate::Error::Io(std::io::Error::new(
    //     //     std::io::ErrorKind::Unsupported,
    //     //     "Not implemented",
    //     // )))
    // }

    pub fn check_permissions(&self) -> crate::Result<PermissionStateValue> {
        Ok(PermissionStateValue {
            value: PermissionState::default(),
        })
    }

    // pub fn get_dir(&self) -> crate::Result<ProviderArgs> {
    //     Ok(ProviderArgs {
    //         alias: None,
    //         path: None,
    //     })
    // }

    // pub fn write_data(&self, args: WriteArgs) -> crate::Result<()> {
    //     let file_uri = format!("{}/{}", args.path_uri, args.file_name);
    //     println!("writing {:?} bytes to `{}`", args.data.len(), file_uri);
    //     let mut file = std::fs::File::create(file_uri).map_err(Error::Io)?;
    //     file.write_all(&args.data).map_err(Error::Io)?;
    //     Ok(())
    // }

    // pub fn exists(&self, args: FileArgs) -> crate::Result<FileAttributes> {
    //     let metadata = std::fs::metadata(args.file_uri).unwrap();

    //     let modified: chrono::DateTime<chrono::Utc> = metadata.modified().unwrap().into();

    //     Ok(FileAttributes {
    //         provider: "Local filesystem".to_string(),
    //         size: metadata.len(),
    //         modification_date: modified.to_rfc3339(),
    //     })
    // }

    // pub fn delete(&self, args: FileArgs) -> crate::Result<String> {
    //     std::fs::remove_file(args.file_uri).unwrap();
    //     Ok("success".to_string())
    // }

    // TODO: check iCloud access locally
    // pub fn check_permissions(&self) -> crate::Result<PermissionStatus> {
    //     Ok(PermissionStatus::default())
    // }
}
