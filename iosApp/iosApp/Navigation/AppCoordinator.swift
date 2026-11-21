import Foundation
import SharedLogic

@MainActor
@Observable
final class AppCoordinator {

    private var navigationEvent: NavigationEvent?
    
    enum Sheet: Identifiable {
        case addTask
        case editTask(Int32)

        var id: String {
            switch self {
            case .addTask:
                return "addTask"
            case .editTask(let taskId):
                return "editTask-\(taskId)"
            }
        }
    }

    var activeSheet: Sheet?
    
    func showAddTask() {
        activeSheet = .addTask
    }

    func showEditTask(taskId: Int32) {
        activeSheet = .editTask(taskId)
    }

    func dismissSheet() {
        activeSheet = nil
    }

    func handleNavigation(_ event: NavigationEvent) {
        switch event {
        case is NavigationEventNavigateToTaskList:
            dismissSheet()
        case is NavigationEventNavigateToAddTask:
            showAddTask()
        case is NavigationEventNavigateToEditTask:
            guard let editEvent = event as? NavigationEventNavigateToEditTask else {
                return
            }
            showEditTask(taskId: editEvent.taskId)
        case is NavigationEventNavigateBack:
            dismissSheet()
        default:
            break
        }
    }
}
