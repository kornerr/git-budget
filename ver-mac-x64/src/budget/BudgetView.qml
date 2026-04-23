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
            Item {
                Layout.fillWidth: true
                height: copy.implicitHeight

                Button {
                    anchors.right: parent.right
                    id: copy
                    text: "Copy"
                    onClicked: api.budgetSet(F.didClickCopy, true)
                }
            }
        }
    }
}

/*
            Item {
                id: resultPanel
                height: result.implicitHeight + 2 * panelPadding
                property int panelPadding: 8
                width: Math.min(310, Math.max(0, parent.width - 2 * panelPadding))

                Rectangle {
                    anchors.fill: parent
                    color: "#a0a0a0"
                    radius: 8
                }
                Rectangle {
                    anchors.fill: parent
                    anchors.margins: 2
                    color: "white"
                    radius: 6

                    ColumnLayout {
                        id: result
                        anchors.fill: parent
                        anchors.margins: 6
                        spacing: 8

                        Label {
                            Layout.fillWidth: true
                            text: vm.result
                        }
                        Item {
                            Layout.fillWidth: true
                            Button {
                                anchors.right: parent.right
                                text: "Copy"
                            }
                        }

                    RowLayout {
                        Layout.fillWidth: true
                        Item {
                            Layout.fillWidth: true
                        }
                    }
                    }
                }
            }
        }
    }
}
*/
