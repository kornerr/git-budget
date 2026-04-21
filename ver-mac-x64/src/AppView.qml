import QtQuick
import QtQuick.Controls

Window {
    height: 480
    id: wnd
    title: qsTr("GitBudget")
    visible: true
    width: 640

    BudgetView {
        anchors.fill: parent
    }
}
