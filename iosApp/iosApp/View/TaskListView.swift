import SwiftUI
import SharedLogic

struct TaskListView: View {
    let adapter: TaskViewModelAdapter

    var body: some View {
        VStack {
            Picker(
                "Task Filter",
                selection: Binding(
                    get: { adapter.selectedFilter },
                    set: { adapter.updateFilter($0) }
                )
            ) {
                Text("All").tag(TaskFilter.all)
                Text("Pending").tag(TaskFilter.pending)
                Text("Completed").tag(TaskFilter.completed)
            }
            .pickerStyle(.segmented)
            .padding(.horizontal)

            // Task list
            List(adapter.filteredTasks, id: \.id) { task in
                TaskRowView(
                    task: task,
                    onToggleCompletion: {
                        adapter.toggleTaskCompletion(id: task.id)
                    },
                    onEdit: {
                        adapter.navigateToEditTask(id: task.id)
                    },
                    onDelete: {
                        adapter.requestDeleteConfirmation(taskId: task.id)
                    }
                )
                .listRowBackground(Color(uiColor: .systemYellow.withAlphaComponent(0.2)))
                .swipeActions(
                    edge: .trailing,
                    allowsFullSwipe: false
                ) {
                    Button {
                        adapter.requestDeleteConfirmation(taskId: task.id)
                    } label: {
                        Label("Delete", systemImage: "trash")
                    }
                    .tint(.red)
                }
            }
            .listStyle(.insetGrouped)
            .scrollContentBackground(.hidden)
        }
        .background(Color(uiColor: .systemGroupedBackground))
    }
}
