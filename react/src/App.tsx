import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import ReportSelect from './OperatSelect';
import Road from "./reports/Road.tsx";
import {createTheme, ThemeProvider} from '@mui/material/styles';
import {CssBaseline, useMediaQuery} from '@mui/material';
import {useMemo} from 'react';
import {Provider} from 'react-redux';
import {store} from "./store/store.ts";

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
        <Provider store={store}>
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
        </Provider>
    )
}

export default App
