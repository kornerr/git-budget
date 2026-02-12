cmake_minimum_required(VERSION 3.16)
project(gb)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)
find_package(Qt6 REQUIRED COMPONENTS Quick)
qt_standard_project_setup(REQUIRES 6.8)
set(SOURCES src/main.cpp src/App.cpp src/budget.cpp src/KT.cpp src/VM.cpp)
qt_wrap_cpp(SOURCES src/App.h src/budget.h src/VM.h)
qt_add_executable(gbmac ${SOURCES})
qt_add_qml_module(
  gbmac
  URI gb
  QML_FILES src/AppView.qml
)

# Подключаем SDK
add_library(gb SHARED IMPORTED)
set_target_properties(gb PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../sdk-mac-x64/gb/build/bin/native/releaseShared/libgb.dylib)

target_include_directories(gbmac PRIVATE ${CMAKE_SOURCE_DIR}/../sdk-mac-x64/gb/build/bin/native/releaseShared)
target_link_libraries(gbmac PRIVATE Qt6::Quick gb)
