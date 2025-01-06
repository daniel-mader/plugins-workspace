import {
  invoke,
  checkPermissions as checkPermissions_
} from '@tauri-apps/api/core'

export type { PermissionState } from '@tauri-apps/api/core'

export interface Status {
  isAvailable: boolean
  error?: string
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
  return await checkPermissions_<{ cloudStorage: PermissionState }>(
    'cloud-storage'
  ).then((r) => r.cloudStorage)
}
