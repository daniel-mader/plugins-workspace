use std::path::PathBuf;

use serde::de::DeserializeOwned;
use tauri::plugin::PermissionState;
use tauri::{
    plugin::{PluginApi, PluginHandle},
    AppHandle, Runtime,
};

use crate::models::*;

#[cfg(target_os = "android")]
const PLUGIN_IDENTIFIER: &str = "app.tauri.cloud_storage";

#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_cloud_storage);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
    _app: &AppHandle<R>,
    api: PluginApi<R, C>,
) -> crate::Result<CloudStorage<R>> {
    #[cfg(target_os = "android")]
    let handle = api.register_android_plugin(PLUGIN_IDENTIFIER, "CloudStoragePlugin")?;
    #[cfg(target_os = "ios")]
    let handle = api.register_ios_plugin(init_plugin_cloud_storage)?;
    Ok(CloudStorage(handle))
}

/// Access to the cloud-storage APIs.
pub struct CloudStorage<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> CloudStorage<R> {
    // pub fn status(&self) -> crate::Result<Status> {
    //     self.0.run_mobile_plugin("status", ()).map_err(Into::into)
    // }

    pub fn check_permissions(&self) -> crate::Result<PermissionStateValue> {
        self.0
            .run_mobile_plugin("checkPermissions", ())
            .map_err(Into::into)
    }

    // pub fn write(&self, payload: WriteArgs) -> crate::Result<WriteResponse> {
    //     self.0
    //         .run_mobile_plugin("write", payload)
    //         .map_err(Into::into)
    // }

    // pub fn exists(&self, args: FileArgs) -> crate::Result<FileAttributes> {
    //     self.0.run_mobile_plugin("exists", ()).map_err(Into::into)
    // }

    // pub fn delete(&self, args: FileArgs) -> crate::Result<String> {
    //     self.0.run_mobile_plugin("delete", ()).map_err(Into::into)
    // }

    pub fn get_dir(&self) -> crate::Result<ProviderArgs> {
        self.0.run_mobile_plugin("getDir", ()).map_err(Into::into)
    }
}
