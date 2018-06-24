package ra.sumbayak.ranalyzer.base;

import ra.sumbayak.ranalyzer.boundary.ProjectWorksheetController;

public class Progress {
    
    private ProjectWorksheetController worksheet;
    private double currentProgress, progressStep;
    
    public Progress (ProjectWorksheetController worksheet) {
        this.worksheet = worksheet;
        progressStep = 0.01;
    }
    
    public void show () {
        worksheet.setLoading (true);
    }
    
    public void setProgressStep (double step) {
        this.progressStep = step;
    }
    
    public boolean makeProgress (double step) {
        currentProgress += step;
        worksheet.setProgress (currentProgress);
        return true;
    }
    
    public boolean makeProgress () {
        return makeProgress (progressStep);
    }
    
    public void dismiss () {
        worksheet.setLoading (false);
    }
}
