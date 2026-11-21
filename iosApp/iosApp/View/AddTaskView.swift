import SwiftUI
import SharedLogic

struct AddTaskView: View {

    @Environment(\.dismiss) private var dismiss
    
    @State private var title = ""
    @State private var description = ""
    @State private var isShowingSuccess = false
    
    @State private var isSaving = false

    let adapter: TaskViewModelAdapter
    let task: Task?
    
    init(adapter: TaskViewModelAdapter, task: Task? = nil) {
        self.adapter = adapter
        self.task = task
        
        _title = State(initialValue: task?.title ?? "")
        _description = State(initialValue: task?.description_ ?? "")
    }

    var body: some View {
        NavigationStack {
            Form {
                Section("Task Details") {
                    TextField("Task Title", text: $title)
                    
                    TextField("Description", text: $description, axis: .vertical).lineLimit(3...5)
                }
                if let errorMessage = adapter.uiState?.errorMessage {
                    Section {
                        Text(errorMessage)
                            .foregroundStyle(.red)
                    }
                }
                if isShowingSuccess {
                    let successMsg = adapter.uiState?.successMessage ?? "Task added successfully"
                    Section {
                        Label(successMsg, systemImage: "checkmark.circle.fill")
                            .foregroundStyle(.green)
                    }
                }
            }
            .navigationTitle(task == nil ? "Add task" : "Edit Task")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        adapter.navigateBack()
                    }
                    .disabled(isSaving)
                }
                
                ToolbarItem(placement: .confirmationAction) {
                    Button {
                        guard !isSaving else { return }
                        isSaving = true
                        let updateTask = Task(
                            id: self.task?.id ?? Int32.random(in: 1...1_000_000_000),
                            title: title.trimmingCharacters(in: .whitespacesAndNewlines),
                            description: description.trimmingCharacters(in: .whitespacesAndNewlines),
                            isCompleted: self.task?.isCompleted ?? false
                        )
                        if self.task == nil {
                            adapter.addTask(updateTask)
                        } else {
                            adapter.updateTask(updateTask)
                        }
                        
                    } label: {
                        if adapter.uiState?.isLoading == true {
                            ProgressView()
                                .progressViewStyle(.circular)
                                .foregroundStyle(.blue)
                            
                        } else {
                            Text("Save").disabled(isShowingSuccess)
                        }
                    }
                    .disabled(title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ||
                              isSaving ||
                              adapter.uiState?.isLoading == true)
                }
            }
            .onChange(of: adapter.uiState?.successMessage) { _, message in
                guard isSaving, message != nil else { return }
                isShowingSuccess = true
                Swift.Task {
                    try? await Swift.Task.sleep(for: .seconds(2))
                    adapter.navigateBack()
                }
            }
        }
    }
}

#Preview {
    let repository = TaskContainer()
    let viewModel = TaskViewModel(
        repository: repository.taskRepository,
        navigationChannel: repository.navigationChannel
    )
    let adapter = TaskViewModelAdapter(viewModel: viewModel)
    AddTaskView(adapter: adapter)
}
