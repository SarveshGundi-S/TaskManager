import SwiftUI
import SharedLogic

@main
struct iOSApp: App {
    private let container = TaskContainer()
    var body: some Scene {
        WindowGroup {
            ContentView(repository: container.taskRepository,
                        navigationChannel: container.navigationChannel)
        }
    }
}
