const COMMANDS: &[&str] = &["status", "check_permissions", "write", "get_dir"];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
