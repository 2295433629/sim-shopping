import request from '@/api/request'
import type { PageResponse } from '@/types/common'

export interface ScheduleJob {
  id: number
  jobName: string
  jobGroup: string
  beanName: string
  methodName: string
  params?: string
  cronExpression?: string
  fixedDelay?: number
  fixedRate?: number
  description?: string
  status: string
  statusText?: string
  concurrent: number
  lastRunTime?: string
  nextRunTime?: string
  lastRunStatus?: string
  lastErrorMsg?: string
  createdAt?: string
  updatedAt?: string
}

export interface ScheduleJobRequest {
  jobName: string
  jobGroup?: string
  beanName: string
  methodName: string
  params?: string
  cronExpression?: string
  fixedDelay?: number
  fixedRate?: number
  description?: string
  concurrent?: number
}

export interface ScheduleLog {
  id: number
  jobId: number
  jobName: string
  status: string
  durationMs: number
  errorMsg?: string
  result?: string
  startTime: string
  endTime?: string
}

export function getScheduleJobs(params: { page: number; size: number; keyword?: string; group?: string }) {
  return request.get<unknown, PageResponse<ScheduleJob>>('/admin/schedule/jobs', { params })
}

export function getScheduleGroups() {
  return request.get<unknown, string[]>('/admin/schedule/jobs/groups')
}

export function getScheduleJobDetail(jobId: number) {
  return request.get<unknown, ScheduleJob>(`/admin/schedule/jobs/${jobId}`)
}

export function createScheduleJob(data: ScheduleJobRequest) {
  return request.post<unknown, ScheduleJob>('/admin/schedule/jobs', data)
}

export function updateScheduleJob(jobId: number, data: ScheduleJobRequest) {
  return request.put<unknown, ScheduleJob>(`/admin/schedule/jobs/${jobId}`, data)
}

export function deleteScheduleJob(jobId: number) {
  return request.delete<unknown, void>(`/admin/schedule/jobs/${jobId}`)
}

export function pauseScheduleJob(jobId: number) {
  return request.put<unknown, void>(`/admin/schedule/jobs/${jobId}/pause`)
}

export function resumeScheduleJob(jobId: number) {
  return request.put<unknown, void>(`/admin/schedule/jobs/${jobId}/resume`)
}

export function executeScheduleJob(jobId: number) {
  return request.post<unknown, void>(`/admin/schedule/jobs/${jobId}/execute`)
}

export function getScheduleJobLogs(jobId: number, params: { page: number; size: number }) {
  return request.get<unknown, PageResponse<ScheduleLog>>(`/admin/schedule/jobs/${jobId}/logs`, { params })
}
