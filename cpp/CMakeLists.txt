cmake_minimum_required(VERSION 3.15)

project(demo)

# 设置 C++ 标准
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

add_executable(demo main.cpp)

add_subdirectory(OpenXLSX)

target_link_libraries(demo PRIVATE OpenXLSX::OpenXLSX)
