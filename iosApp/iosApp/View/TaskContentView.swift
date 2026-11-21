import SwiftUI
import SharedLogic

struct TaskContentView: View {
    let adapter: TaskViewModelAdapter

    var body: some View {
        Group {
            if let state = adapter.uiState {
                if state.isLoading && state.tasks.isEmpty {
                    ProgressView("Loading tasks...")

                } else if let message = state.errorMessage {
                    Text(message)
                        .foregroundStyle(.red)

                } else if state.tasks.isEmpty {
                    ContentUnavailableView {
                        Label("No tasks yet", systemImage: "checklist")
                    } description: {
                        Text(
                            "You haven't added any tasks. Create your first task to get started."
                        )
                    } actions: {
                        Button {
                            adapter.navigateToAddTask()
                        } label: {
                            Label("Add Task", systemImage: "plus")
                        }
                        .buttonStyle(.borderedProminent)
                    }

                } else {
                    TaskListView(
                        adapter: adapter
                    )
                }
            } else {
                ProgressView("Loading tasks...")
                    .progressViewStyle(.linear)
                    .foregroundStyle(.gray)
            }
        }
    }
}
