import SwiftUI
import SharedLogic

struct ContentView: View {
    @State private var adapter: TaskViewModelAdapter
    
    @State private var coordinator = AppCoordinator()
    
    init(repository: TaskRepository,
         navigationChannel: NavigationChannel) {
        let viewModel = TaskViewModel(repository: repository, navigationChannel: navigationChannel)
        adapter = TaskViewModelAdapter(viewModel: viewModel)
    }

    var body: some View {
        NavigationStack {
            TaskContentView(adapter: adapter)
            .navigationTitle("Task Manager")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    if let state = adapter.uiState,
                       !state.tasks.isEmpty {
                        Button {
                            adapter.navigateToAddTask()
                        } label: {
                            Image(systemName: "plus")
                        }
                    }
                }
                .hideSharedBackgroundOnModerniOS()
            }
            .sheet(item: $coordinator.activeSheet) { sheet in
                switch sheet {
                case .addTask:
                    AddTaskView(adapter: adapter, task: nil)
                case .editTask(let taskId):
                    if let task = adapter.uiState?.tasks.first(where: {$0.id == taskId}) {
                        AddTaskView(adapter: adapter, task: task)
                    } else {
                        Text("Task not found")
                    }
                }
            }
            
            .alert("Delete Task?",
                   isPresented: Binding (
                    get: {adapter.isDeleteConfirmationVisible},
                    set: {isPresent in
                        if !isPresent {
                            adapter.dismissDeleteConfirmation()
                        }
                    }
                   )
            ) {
                Button("Delete", role: .destructive) {
                    adapter.confirmDelete()
                }
                
                Button("Cancel", role: .cancel) {
                    adapter.dismissDeleteConfirmation()
                }
            } message: {
                Text("Are you sure you want to delete this task? this action can not be undone.")
            }
        }
        .task {
            adapter.onNavigationEvent = { event in
                coordinator.handleNavigation(event)
            }
            adapter.startObserving()
            adapter.fetchTasks()
        }
        .onDisappear {
            adapter.stopObserving()
            adapter.onNavigationEvent = nil
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        let container = TaskContainer()

        ContentView(repository: container.taskRepository, navigationChannel: container.navigationChannel)
    }
}

extension ToolbarContent {
    @ToolbarContentBuilder
    func hideSharedBackgroundOnModerniOS() -> some ToolbarContent {
        if #available(iOS 26.0, *) {
            self.sharedBackgroundVisibility(.hidden)
        } else {
            self
        }
    }
}
