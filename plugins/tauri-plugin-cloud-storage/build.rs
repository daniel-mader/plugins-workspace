const COMMANDS: &[&str] = &["ping", "status", "check_permissions", "write"];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
