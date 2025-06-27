import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import ReportSelect from './OperatSelect';
import Road from "./reports/Road.tsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/reports" replace/>}/>
                <Route path="/reports" element={<ReportSelect/>}/>
                <Route path="/reports/road" element={<Road/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default App
