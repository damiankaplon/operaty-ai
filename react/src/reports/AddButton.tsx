import * as React from "react";
import {useRef} from "react";
import {Fab} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import type {RoadReport} from "./RoadReport.ts";


async function handleFileUpload(
    event: React.ChangeEvent<HTMLInputElement>,
    onSuccess: (roadReport: RoadReport) => void,
) {
    const file = event.target.files?.[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('/api/reports/road', {
            method: 'POST',
            body: formData
        });
        const data = await response.json() as RoadReport;
        onSuccess(data);
    } catch (error) {
        console.error('Error uploading file:', error);
    }
}

export function AddButton(
    {onSuccess}: { onSuccess: (roadReport: RoadReport) => void, }
) {
    const fileInputRef = useRef<HTMLInputElement>(null);
    return (
        <>
            <input
                type="file"
                ref={fileInputRef}
                style={{display: 'none'}}
                onChange={(event => handleFileUpload(event, onSuccess))}
                accept=".pdf"
            />
            <Fab
                color="primary"
                style={{
                    position: 'fixed',
                    bottom: 80,
                    right: 16,
                }}
                onClick={() => fileInputRef.current?.click()}
            >
                <AddIcon/>
            </Fab>
        </>
    );
}
