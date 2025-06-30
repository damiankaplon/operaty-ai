import {DataGrid, type GridColDef} from "@mui/x-data-grid";
import * as React from "react";
import type {RoadReport} from "./RoadReport.ts";
import {AddButton} from "./AddButton.tsx";


const COLUMNS: GridColDef[] = [
    {field: 'reportNumber', headerName: 'Numer', editable: true},
    {field: 'area', headerName: 'Gdzie', editable: true},
    {field: 'roadNumber', headerName: 'Numer drogi', editable: true},
    {field: 'from', headerName: 'Od', editable: true},
    {field: 'to', headerName: 'Do', editable: true},
    {field: 'miscellaneous', headerName: 'Dodatkowe', editable: true},
    {field: 'detailed', headerName: 'Wyszczególnione elementy', editable: true},
    {field: 'task', headerName: 'Zadanie', editable: true},
    {field: 'report', headerName: 'Operat', editable: true},
    {field: 'measurementDate', headerName: 'Data pomiaru', editable: true},
    {field: 'reportDate', headerName: 'Data operatu', editable: true},
    {field: 'length', headerName: 'Długość', editable: true},
    {field: 'loweredCurb', headerName: 'Kr. obniżony', editable: true},
    {field: 'rim', headerName: 'Obrzeże', editable: true},
    {field: 'inOut', headerName: 'W/WY', editable: true},
    {field: 'flat', headerName: 'Płaskie', editable: true},
    {field: 'pa', headerName: 'PA', editable: true},
    {field: 'slope', headerName: 'Skarpy', editable: true},
    {field: 'ditch', headerName: 'Rowy', editable: true},
    {field: 'demolition', headerName: 'Rozbiórka', editable: true},
    {field: 'surface', headerName: 'Powierzchnia', editable: true},
    {field: 'volume', headerName: 'Objętość', editable: true},
    {field: 'inner', headerName: 'wew', editable: true},
    {field: 'odh', headerName: 'Grubość ODH', editable: true},
    {field: 'dig', headerName: 'WYMIANA [wykop]', editable: true},
    {field: 'infill', headerName: 'WYMIANA [zasyp]', editable: true},
    {field: 'bank', headerName: 'Nasyp', type: 'number', editable: true},
    {field: 'excavation', headerName: 'Wykop', type: 'number', editable: true},
];

export default function Road() {
    const [rows, setRows] = React.useState<RoadReport[]>([]);
    return (
        <div style={{width: '100vw', height: '100vh', backgroundColor: '#f5f5f5'}}>
            <div style={{height: '100%', width: '100%'}}>
                <DataGrid
                    rows={rows}
                    isRowSelectable={() => true}
                    columns={COLUMNS}
                    getRowId={(row) => row.reportNumber}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 10},
                        },
                    }}
                    pageSizeOptions={[5, 10, 25]}
                />
            </div>
            <AddButton onSuccess={(roadReport) => setRows((oldRows) => [...oldRows, roadReport])}/>
        </div>
    )
}
