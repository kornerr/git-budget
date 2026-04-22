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
                    onTextChanged: api.budgetSet(F.inputSpent, text)
                    placeholderText: "Spent"
                    text: vm.spent
                }
                Button {
                    onClicked: api.budgetSet(F.didClickPasteSpent, true)
                    text: "Paste"
                }
            }
            RowLayout {
                Layout.fillWidth: true
        
                TextField {
                    Layout.fillWidth: true

                    onTextChanged: api.budgetSet(F.inputMorningBalance, text)
                    placeholderText: "Morning balance"
                    text: vm.morningBalance
                }
                Button {
                    onClicked: api.budgetSet(F.didClickPasteMorningBalance, true)
                    text: "Paste"
                }
            }
        }
    }
}

/*
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
*/
