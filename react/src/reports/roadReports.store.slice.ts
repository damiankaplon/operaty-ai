import {createSlice, type PayloadAction} from '@reduxjs/toolkit';
import type {RoadReport} from './RoadReport';

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
          // Replace the old report with the confirmed one
        state.reports[index] = {
            ...action.payload,
            edited: false
        };
      }
    }
  }
});

export const {
  addRoadReport, 
  updateRoadReport, 
  confirmRoadReportEdit 
} = roadReportsSlice.actions;

