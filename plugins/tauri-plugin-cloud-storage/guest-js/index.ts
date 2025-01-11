import {
  invoke,
  checkPermissions as checkPermissions_
} from '@tauri-apps/api/core'

export type { PermissionState } from '@tauri-apps/api/core'

export interface Status {
  isAvailable: boolean
  error?: string
}

export interface FileAttributes {
  provider: string
  size: number
  modificationDate: string
}

export interface FileArgs {
  fileUri: string
}

/**
 * `fileUri` is only required for Android
 */
export interface WriteArgs {
  fileUri?: string
  value: string
}

export interface Value {
  value: string
}

export async function ping(value: string): Promise<string | null> {
  return await invoke<{ value?: string }>('plugin:cloud-storage|ping', {
    payload: {
      value
    }
  }).then((r) => (r.value ? r.value : null))
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
export async function writeBytes(args: WriteArgs): Promise<Value> {
  return await invoke('plugin:cloud-storage|write', { args })
}

/**
 * Checks if a backup file exists and returns its "last modified" date and its size.
 */
export async function exists(args: FileArgs): Promise<FileAttributes> {
  return await invoke('plugin:cloud-storage|exists', { args })
}

/**
 * Deletes the backup file.
 */
export async function deleteBackup(args: FileArgs): Promise<void> {
  return await invoke('plugin:cloud-storage|delete', { args })
}
