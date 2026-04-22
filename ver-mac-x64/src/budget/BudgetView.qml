import QtQuick
import QtQuick.Controls
import QtQuick.Layouts
import QtQuick.Window

Rectangle {
    id: budgetView
    color: Window.window ? Window.window.color : palette.window

    SystemPalette {
        id: palette
        colorGroup: SystemPalette.Active
    }

    ColumnLayout {
        anchors.fill: parent
        anchors.leftMargin: 12
        anchors.rightMargin: 12
        spacing: 8

        RowLayout {
            Layout.fillWidth: true
            spacing: 8

            TextField {
                id: spentField
                Layout.fillWidth: true
                placeholderText: qsTr("Spent")
                selectByMouse: true
                text: vm.spent
                onTextChanged: api.budgetSet(F.inputSpent, text)
            }

            Button {
                highlighted: true
                text: qsTr("Paste")
                onClicked: api.budgetSet(F.didClickPasteSpent, true)
            }
        }

        RowLayout {
            Layout.fillWidth: true
            spacing: 8

            TextField {
                id: morningField
                Layout.fillWidth: true
                placeholderText: qsTr("Morning balance")
                selectByMouse: true
                text: vm.morningBalance
                onTextChanged: api.budgetSet(F.inputMorningBalance, text)
            }

            Button {
                highlighted: true
                text: qsTr("Paste")
                onClicked: api.budgetSet(F.didClickPasteMorningBalance, true)
            }
        }

        Rectangle {
            Layout.fillWidth: true
            color: "transparent"
            border.color: Qt.rgba(0.5, 0.5, 0.5, 0.3)
            border.width: 1
            radius: 8

            ColumnLayout {
                anchors.fill: parent
                anchors.margins: 8
                spacing: 8

                Label {
                    Layout.fillWidth: true
                    wrapMode: Text.Wrap
                    text: vm.result
                }

                RowLayout {
                    Layout.fillWidth: true
                    spacing: 0

                    Item {
                        Layout.fillWidth: true
                    }

                    Button {
                        highlighted: true
                        text: qsTr("Copy")
                        onClicked: api.budgetSet(F.didClickCopy, true)
                    }
                }
            }
        }

        Item {
            Layout.fillHeight: true
        }
    }

    Connections {
        target: vm

        function onSpentChanged() {
            if (!spentField.activeFocus) {
                spentField.text = vm.spent
            }
        }

        function onMorningBalanceChanged() {
            if (!morningField.activeFocus) {
                morningField.text = vm.morningBalance
            }
        }
    }
}
