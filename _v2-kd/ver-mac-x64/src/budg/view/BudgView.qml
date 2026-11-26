import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Rectangle {
    anchors.fill: parent
    Rectangle {
        anchors.centerIn: parent
        height: card.implicitHeight + 2 * padding
        property int padding: 24
        width: Math.min(320, Math.max(0, parent.width - 2 * padding))

        ColumnLayout {
            anchors.fill: parent
            anchors.margins: parent.padding
            id: card

            RowLayout {
                Layout.fillWidth: true
        
                TextField {
                    Layout.fillWidth: true
                    onTextChanged: api.budgSet(F.inputSpent, text)
                    placeholderText: vm.spentPlaceholder
                    text: vm.spent
                }
                Button {
                    onClicked: api.budgSet(F.didClickPasteSpent, true)
                    text: vm.spentPasteTitle
                }
            }
            RowLayout {
                Layout.fillWidth: true
        
                TextField {
                    Layout.fillWidth: true

                    onTextChanged: api.budgSet(F.inputMorningBalance, text)
                    placeholderText: vm.morningBalancePlaceholder
                    text: vm.morningBalance
                }
                Button {
                    onClicked: api.budgSet(F.didClickPasteMorningBalance, true)
                    text: vm.morningBalancePasteTitle
                }
            }
            Rectangle {
                border.color: "lightgray"
                border.width: 1
                height: res.implicitHeight + 1.5 * padding
                Layout.fillWidth: true
                property int padding: 8
                radius: 8

                ColumnLayout {
                    anchors.fill: parent
                    anchors.margins: parent.padding
                    id: res

                    Label {
                        text: vm.result
                    }
                    Item {
                        Layout.fillWidth: true
                        height: copy.implicitHeight
                   
                        Button {
                            anchors.right: parent.right
                            id: copy
                            text: vm.resultCopyTitle
                            onClicked: api.budgSet(F.didClickCopy, true)
                        }
                    }
                }
            }
        }
    }
}
