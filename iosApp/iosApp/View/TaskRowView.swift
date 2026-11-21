import SwiftUI
import SharedLogic

struct TaskRowView: View {
    let task: Task
    let onToggleCompletion: () -> Void
    let onEdit: () -> Void
    let onDelete: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            Button(action: onToggleCompletion) {
                Image(systemName: task.isCompleted ? "checkmark.circle.fill" : "circle")
                    .font(.title2)
                    .foregroundStyle(task.isCompleted ? .green : .secondary)
            }
            .buttonStyle(.plain)
            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text(task.title)
                        .font(.headline)
                        .strikethrough(task.isCompleted)
                        .foregroundStyle(
                            task.isCompleted ? .secondary : .primary
                        )
                    
                    Text(task.isCompleted ? "Completed" : "Pending")
                        .font(.callout)
                        .foregroundStyle(
                            task.isCompleted ? .green : .secondary
                        )
                }
                
                Text(task.description_)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
            }
            Spacer()
            HStack(spacing: 16) {
                Button(action: onEdit) {
                    Image(systemName: "pencil.circle.fill")
                        .font(.title2)
                        .foregroundStyle(.tint)
                }

                Button(role: .destructive, action: onDelete) {
                    Image(systemName: "trash")
                        .font(.title3)
                        .foregroundStyle(.red)
                }
            }
            .buttonStyle(.plain)
        }
    }
}

#Preview {
    
}
