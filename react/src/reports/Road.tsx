import {DataGrid, type GridColDef} from "@mui/x-data-grid";

interface RoadReport {
    id: number | undefined;
    reportNumber: string | undefined;
    area: string | undefined;
    roadNumber: string | undefined;
    from: string | undefined;
    to: string | undefined;
    miscellaneous: string | undefined;
    detailed: string | undefined;
    task: string | undefined;
    report: string | undefined;
    measurementDate: string | undefined;
    reportDate: string | undefined;
    bank: number | undefined;
    excavation: number | undefined;
}

export default function Road() {
    const columns: GridColDef[] = [
        {field: 'reportNumber', headerName: 'Numer operatu', editable: true},
        {field: 'area', headerName: 'Gdzie', editable: true},
        {field: 'roadNumber', headerName: 'Numer drogi', editable: true},
        {field: 'from', headerName: 'km od', editable: true},
        {field: 'to', headerName: 'do km', editable: true},
        {field: 'miscellaneous', headerName: 'dodatkowe', editable: true},
        {field: 'detailed', headerName: 'Wyszczególnione elementy', editable: true},
        {field: 'task', headerName: 'szczegółowe zadanie', editable: true},
        {field: 'report', headerName: 'OPERAT', editable: true},
        {field: 'measurementDate', headerName: 'data pomiaru', editable: true},
        {field: 'reportDate', headerName: 'data operatu', editable: true},
        {field: 'length', headerName: 'Długość', editable: true},
        {field: 'loweredCurb', headerName: 'KR.OBNIŻONY', editable: true},
        {field: 'rim', headerName: 'OBRZEZE', editable: true},
        {field: 'inOut', headerName: 'W-WY', editable: true},
        {field: 'flat', headerName: 'PLASKIE', editable: true},
        {field: 'pa', headerName: 'PA', editable: true},
        {field: 'slope', headerName: 'SKARPY', editable: true},
        {field: 'ditch', headerName: 'ROWY', editable: true},
        {field: 'demolition', headerName: 'rozbiorka', editable: true},
        {field: 'surface', headerName: 'POWIERZCHNIA', editable: true},
        {field: 'volume', headerName: 'OBJETOSC', editable: true},
        {field: 'inner', headerName: 'wew', editable: true},
        {field: 'odh', headerName: 'GRUBOŚĆ ODH', editable: true},
        {field: 'dig', headerName: 'WYMIANA [wykop]', editable: true},
        {field: 'infill', headerName: 'WYMIANA [zasyp]', editable: true},
        {field: 'bank', headerName: 'NASYP', type: 'number', editable: true},
        {field: 'excavation', headerName: 'WYKOP', type: 'number', editable: true},
    ];

    const rows: RoadReport[] = [
        {
            id: 1,
            reportNumber: 'D-156',
            area: 'Koszalin',
            roadNumber: 'S11',
            from: '',
            to: undefined,
            miscellaneous: undefined,
            detailed: undefined,
            task: undefined,
            report: undefined,
            measurementDate: undefined,
            reportDate: undefined,
            bank: undefined,
            excavation: undefined
        }
    ];

    return (
        <div style={{width: '100vw', height: '100vh', backgroundColor: '#f5f5f5'}}>
            <div style={{height: '100%', width: '100%'}}>
                <DataGrid
                    rows={rows}
                    isRowSelectable={()=>true}
                    columns={columns}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 10},
                        },
                    }}
                    pageSizeOptions={[5, 10, 25]}
                />
            </div>
        </div>
    )
}
