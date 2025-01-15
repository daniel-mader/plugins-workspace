// #![cfg(mobile)]

use tauri::{
    plugin::{Builder, PluginHandle, TauriPlugin},
    Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::CloudStorage;
#[cfg(mobile)]
use mobile::CloudStorage;

// pub struct CloudStorage<R: Runtime>(PluginHandle<R>);

// impl<R: Runtime> CloudStorage<R> {}

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the cloud-storage APIs.
pub trait CloudStorageExt<R: Runtime> {
    fn cloud_storage(&self) -> &CloudStorage<R>;
}

impl<R: Runtime, T: Manager<R>> crate::CloudStorageExt<R> for T {
    fn cloud_storage(&self) -> &CloudStorage<R> {
        self.state::<CloudStorage<R>>().inner()
    }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
    Builder::new("cloud-storage")
        .invoke_handler(tauri::generate_handler![
            commands::check_permissions,
            // commands::delete,
            #[cfg(mobile)]
            commands::get_dir,
        ])
        .setup(|app, api| {
            #[cfg(mobile)]
            let cloud_storage = mobile::init(app, api)?;
            #[cfg(desktop)]
            let cloud_storage = desktop::init(app, api)?;
            app.manage(cloud_storage);
            // #[cfg(target_os = "android")]
            // let handle = api.register_android_plugin(PLUGIN_IDENTIFIER, "CloudStoragePlugin")?;
            // #[cfg(target_os = "ios")]
            // let handle = api.register_ios_plugin(init_plugin_cloud_storage)?;
            // app.manage(CloudStorage(handle));
            Ok(())
        })
        .build()
}
