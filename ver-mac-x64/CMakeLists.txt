cmake_minimum_required(VERSION 3.16)
project(gitbudget)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)
find_package(Qt6 REQUIRED COMPONENTS Quick Widgets)
qt_standard_project_setup(REQUIRES 6.8)

# App
set(
    SOURCES
    src/App.cpp
    src/budget/budget.cpp
    src/budget/budgetAux.cpp
    src/budget/budgetEffect.cpp
    src/ignore.kd.cpp
    src/KT.cpp
    src/VM.cpp
)
qt_wrap_cpp(
    SOURCES
    src/ignore.kd.h
    src/VM.h
)
qt_add_executable(gbM64 ${SOURCES})
qt_add_qml_module(
    gbM64
    URI gitbudget
    QML_FILES
    src/budget/BudgetView.qml
    src/AppView.qml
)

# SDK
add_library(gb SHARED IMPORTED)
set_target_properties(gb PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../sdk-mac-x64/gb/build/bin/native/releaseShared/libgb.dylib)
target_include_directories(
    gbM64
    PRIVATE
    ${CMAKE_SOURCE_DIR}/../sdk-mac-x64/gb/build/bin/native/releaseShared
    src
    src/budget
)

# Link
target_link_libraries(gbM64 PRIVATE gb Qt6::Quick Qt6::Widgets)
