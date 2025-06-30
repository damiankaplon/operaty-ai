import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import ReportSelect from './OperatSelect';
import Road from "./reports/Road.tsx";
import {ThemeProvider, createTheme} from '@mui/material/styles';
import {CssBaseline, useMediaQuery} from '@mui/material';
import {useMemo} from 'react';

function App() {
    const prefersDarkMode = useMediaQuery('(prefers-color-scheme: dark)');

    const theme = useMemo(
        () =>
            createTheme({
                palette: {
                    mode: prefersDarkMode ? 'dark' : 'light',
                },
            }),
        [prefersDarkMode],
    );

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Navigate to="/reports" replace/>}/>
                    <Route path="/reports" element={<ReportSelect/>}/>
                    <Route path="/reports/road" element={<Road/>}/>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    )
}

export default App
