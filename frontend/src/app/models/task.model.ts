export interface Task {
    id: number,
    title: String,
    description: String,
    status: String,
    createdAt: String,
    updatedAt: String
}

export interface TaskRequest {
    title: String,
    description: String,
    status: String,
}