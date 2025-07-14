import {createAsyncThunk, createSlice, type PayloadAction} from '@reduxjs/toolkit';
import type {RoadReport} from './RoadReport';
import axios from "axios";

interface RoadReportsState {
  reports: RoadReport[];
}

const initialState: RoadReportsState = {
  reports: [
      {
          reportNumber: 5555,
          area: 'Warszawa Południe',
          roadNumber: 'DK-8',
          from: '78+500',
          to: '79+800',
          miscellaneous: 'Modernizacja nawierzchni',
          detailed: 'Nawierzchnia, pobocza',
          task: 'Zadanie 4/2025',
          report: 'Operat 2/2025',
          measurementDate: '2025-02-01',
          reportDate: '2025-02-10',
          length: "1300",
          loweredCurb: "95.2",
          rim: "380.5",
          inOut: "65.8",
          flat: "620.3",
          pa: "185.4",
          slope: "275.6",
          ditch: "155.2",
          demolition: "82.4",
          surface: "3900.5",
          volume: "680.2",
          inner: "110.5",
          odh: "38.7",
          dig: "245.3",
          infill: "230.4",
          bank: 165.2,
          excavation: 190.8,
          edited: false
      }
  ]
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

export const editRoadReport = createAsyncThunk(
    'roadReports/editRoadReport',
    async (report: RoadReport, { dispatch, rejectWithValue }) => {
      try {
         dispatch(confirmRoadReportEdit(report));
        return  await axios.post(`/api/reports/road/${report.reportNumber}/version`, report);
      } catch (error) {
        if (axios.isAxiosError(error)) {
          return rejectWithValue(error.response?.data ?? error);
        }
        return rejectWithValue(error);
      }
    }
);


