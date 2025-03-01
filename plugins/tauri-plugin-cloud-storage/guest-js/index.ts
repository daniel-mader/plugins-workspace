import {
  invoke,
  checkPermissions as checkPermissions_
} from '@tauri-apps/api/core'

export type { PermissionState } from '@tauri-apps/api/core'

export interface Status {
  isAvailable: boolean
  error?: string
}

export interface FileArgs {
  fileUri: string
}

/**
 * `fileUri` is only required for Android
 */
export interface WriteArgs {
  pathUri?: string
  fileName: string
  data: Uint8Array
}

export interface ProviderArgs {
  alias?: string // "iCloud", "Google Drive", "Local filesystem"
  path?: string
}

export interface Value {
  value: string
}

export async function status(): Promise<Status> {
  return await invoke('plugin:cloud-storage|status')
}

/**
 * Get permission state.
 */
export async function checkPermissions(): Promise<PermissionState> {
  return await checkPermissions_<{ value: PermissionState }>(
    'cloud-storage'
  ).then((r) => r.value)
}

/**
 * Write bytes.
 * @param options
 */
export async function writeData(args: WriteArgs): Promise<void> {
  return await invoke('plugin:cloud-storage|write_data', { args })
}

/**
 * Deletes the backup file.
 */
export async function deleteBackup(args: FileArgs): Promise<void> {
  return await invoke('plugin:cloud-storage|delete', { args })
}

/**
 * Gets the directory of the backup files.
 */
export async function getDir(): Promise<ProviderArgs> {
  return await invoke('plugin:cloud-storage|get_dir')
}
