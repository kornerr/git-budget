cmake_minimum_required(VERSION 3.16)
project(gb)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)
find_package(Qt6 REQUIRED COMPONENTS Quick)
qt_standard_project_setup(REQUIRES 6.8)
set(SOURCES src/main.cpp src/App.cpp src/VM.cpp)
qt_wrap_cpp(SOURCES src/App.h src/VM.h)
qt_add_executable(gbmac ${SOURCES})
qt_add_qml_module(
  gbmac
  URI gb
  QML_FILES src/AppView.qml
)
target_link_libraries(gbmac PRIVATE Qt6::Quick)
