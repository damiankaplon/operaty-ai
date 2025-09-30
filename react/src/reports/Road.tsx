import {DataGrid, GridActionsCellItem, type GridColDef} from "@mui/x-data-grid";
import * as React from "react";
import type {RoadReport} from "./RoadReport.ts";
import {AddButton} from "./AddButton.tsx";
import CheckIcon from '@mui/icons-material/Check';
import {useAppDispatch, useAppSelector} from '../store/hooks.ts';
import {addRoadReport, editRoadReport, fetchRoadReports, updateRoadReport} from './roadReports.store.slice.ts';

const COLUMNS: (onEditConfirmation: (report: RoadReport) => void) => GridColDef<RoadReport>[] = (onEditConfirmation) => [
    {
        field: 'action',
        headerName: 'Potwierdź',
        sortable: false,
        filterable: false,
        renderCell: params => {
            if (params.row.edited) {
                return (
                    <GridActionsCellItem
                        icon={<CheckIcon/>}
                        label="Confirm"
                        onClick={() => onEditConfirmation(params.row)}
                        color="inherit"
                    />
                );
            } else {
                return null;
            }
        }
    },
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
    {field: 'excavation', headerName: 'Wykop', type: 'number', editable: true}
];

export default function Road() {
    const [isLoading, setIsLoading] = React.useState(false);
    const dispatch = useAppDispatch();
    const rows = useAppSelector(state => state.roadReports.reports);

    React.useEffect(() => {
        let canceled = false;
        (async () => {
            setIsLoading(true);
            try {
                await dispatch(fetchRoadReports());
            } finally {
                if (!canceled) setIsLoading(false);
            }
        })();
        return () => {
            canceled = true;
        };
    }, [dispatch]);

    return (
        <div style={{width: '100vw', height: '100vh', backgroundColor: '#f5f5f5'}}>
            <div style={{height: '100%', width: '100%'}}>
                <DataGrid
                    rows={rows}
                    isRowSelectable={() => true}
                    columns={COLUMNS((report: RoadReport) => dispatch(editRoadReport(report)))}
                    getRowId={(row: RoadReport) => row.reportNumber}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 10},
                        },
                    }}
                    pageSizeOptions={[5, 10, 25]}
                    loading={isLoading}
                    slotProps={{
                        loadingOverlay: {
                            variant: 'linear-progress',
                            noRowsVariant: 'linear-progress'
                        }
                    }}
                    processRowUpdate={(newRow: RoadReport) => {
                        dispatch(updateRoadReport(newRow));
                        return rows.find(row => row.reportNumber === newRow.reportNumber)!;
                    }}
                    onProcessRowUpdateError={console.error}
                />
            </div>
            <AddButton onSuccess={(roadReport) => {
                dispatch(addRoadReport(roadReport));
                setIsLoading(false);
            }} onLoad={() => setIsLoading(true)}
            />
        </div>
    )
}
