#include <iostream>
#include <string>
#include <chrono>

#include <OpenXLSX.hpp>

int main()
{
    int i = 0;
    std::string filePath = "D:/nick/code/ExcelDemo/java/QuickStart/target/simpleWrite1732122731848.xlsx";
    OpenXLSX::XLDocument doc;
    doc.open(filePath);
    
    auto wks = doc.workbook().worksheet("test");
    
    auto start = std::chrono::high_resolution_clock::now();
    for (OpenXLSX::XLRow row : wks.rows()) 
    {
        for (OpenXLSX::XLCell cell : row.cells())
        {
            i++;
        }
    }
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> elapsed = end - start;
    std::cout << "Elapsed time: " << elapsed.count() << " seconds." << std::endl;
    std::cout << "cell : " << i << std::endl;

    doc.close();
    
    return 0;
}