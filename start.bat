@echo off
echo ========================================
echo    Finnancy Spring API - Iniciando
echo ========================================
echo.
echo Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java no encontrado. Instala Java 21 o superior.
    pause
    exit /b 1
)

echo.
echo Compilando y ejecutando la aplicacion...
echo.
call mvnw.cmd spring-boot:run

pause
