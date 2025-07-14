import {configureStore} from "@reduxjs/toolkit";
import {roadReportsSlice} from '../reports/roadReports.store.slice.ts';

export const store = configureStore({
    reducer: {
        roadReports: roadReportsSlice.reducer
    },
});

export type AppState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
