import http from '@/utils/http'

// 系统创建
export function generatorCreateApi(params) {
  return http.post('/factory/generator/create', params);
}

// 系统创建
export function getFileTreeApi(params) {
  return http.post('/factory/generator/file-tree', params);
}

// 系统创建
export function getFileApi(params) {
  return http.get('/factory/generator/file', params);
}

// 系统创建
export function downloadApi(params, onDownloadProgress) {
  return http.download('/factory/generator/download', params, onDownloadProgress)
}
