import Foundation
import Observation
import SharedLogic

@MainActor
@Observable
final class TaskViewModelAdapter {
    private(set) var uiState: TaskUiState?
    var selectedFilter: TaskFilter {
        uiState?.selectedFilter ?? .all
    }

    var isDeleteConfirmationVisible: Bool {
        uiState?.isDeleteConfirmationVisible ?? false
    }

    var taskToDeleteById: Int32? {
        uiState?.taskToDeleteById?.int32Value
    }

    private let viewModel: TaskViewModel
    private var observation: Closeable?
    private var navigationObservation: Closeable?
    
    var onNavigationEvent: ((NavigationEvent) -> Void)?

    init(viewModel: TaskViewModel) {
        self.viewModel = viewModel
    }

    func startObserving() {
        guard observation == nil else { return }

        observation = viewModel.getTaskViewModel.watch { [weak self] state in
            MainActor.assumeIsolated {
                self?.uiState = state
            }
        }

        navigationObservation = viewModel.getNavigationEvents.watch { [weak self] event in
            MainActor.assumeIsolated {
                self?.onNavigationEvent?(event)
            }
        }
    }

    func fetchTasks() {
        viewModel.fetchTasks()
    }

    func updateTask(_ task: Task) {
        viewModel.updateTask(task: task)
    }

    func addTask(_ task: Task) {
        viewModel.addTask(task: task)
    }

    func toggleTaskCompletion(id: Int32) {
        viewModel.toggleTaskCompletion(taskId: id)
    }

    func deleteTask(id: Int32) {
        viewModel.delete(taskId: id)
    }

    var filteredTasks: [Task] {
        guard let tasks = uiState?.tasks else {
            return []
        }
        
        switch selectedFilter {
        case .all:
            return tasks
        case .pending:
            return tasks.filter { !$0.isCompleted }
        case .completed:
            return tasks.filter { $0.isCompleted }
        default:
            return tasks
        }
    }

    func updateFilter(_ filter: TaskFilter) {
        viewModel.updateFilter(filter: filter)
    }

    func requestDeleteConfirmation(taskId: Int32) {
        viewModel.requestDeleteConfirmation(taskId: taskId)
    }

    func dismissDeleteConfirmation() {
        viewModel.dismissDeleteConfirmation()
    }

    func confirmDelete() {
        viewModel.confirmDelete()
    }

    func stopObserving() {
        observation?.cancel()
        observation = nil
        
        navigationObservation?.cancel()
        navigationObservation = nil
    }

    func navigateToAddTask() {
        viewModel.navigateToAddTask()
    }

    func navigateToEditTask(id: Int32) {
        viewModel.navigateToEditTask(taskId: id)
    }

    func navigateBack() {
        viewModel.navigateBack()
    }
}
