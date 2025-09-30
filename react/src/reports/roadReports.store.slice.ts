import {createAsyncThunk, createSlice, type PayloadAction} from '@reduxjs/toolkit';
import type {RoadReport} from './RoadReport';
import axios from "axios";

interface RoadReportsState {
  reports: RoadReport[];
}

const initialState: RoadReportsState = {
    reports: []
};

export const roadReportsSlice = createSlice({
  name: 'roadReports',
  initialState,
  reducers: {
      setRoadReports: (state, action: PayloadAction<RoadReport[]>) => {
          state.reports = action.payload;
      },

    addRoadReport: (state, action: PayloadAction<RoadReport>) => {
      state.reports.push(action.payload);
    },
    
    updateRoadReport: (state, action: PayloadAction<RoadReport>) => {
      const index = state.reports.findIndex(
        report => report.reportNumber === action.payload.reportNumber
      );
      if (index !== -1) {
        state.reports[index] = {
            ...action.payload,
            edited: true
        };
      }
    },
    
    confirmRoadReportEdit: (state, action: PayloadAction<RoadReport>) => {
      const index = state.reports.findIndex(
        report => report.reportNumber === action.payload.reportNumber
      );
      if (index !== -1) {
        state.reports[index] = {
            ...action.payload,
            edited: false
        };
      }
    }
  }
});

export const {
    setRoadReports,
  addRoadReport, 
  updateRoadReport, 
  confirmRoadReportEdit 
} = roadReportsSlice.actions;

export const editRoadReport = createAsyncThunk(
    'roadReports/editRoadReport',
    async (report: RoadReport, { dispatch, rejectWithValue }) => {
      try {
         dispatch(confirmRoadReportEdit(report));
        return  await axios.post(`/api/reports/road/${report.reportNumber}/version`, report);
      } catch (error) {
        return rejectWithValue(error);
      }
    }
);

export const fetchRoadReports = createAsyncThunk(
    'roadReports/fetchRoadReports',
    async (_: void, {dispatch, rejectWithValue}) => {
        try {
            const response = await axios.get<RoadReport[]>('/api/reports/road');
            const reports = response.data ?? [];
            dispatch(setRoadReports(reports));
            return reports;
        } catch (error) {
            return rejectWithValue(error);
        }
    }
);
