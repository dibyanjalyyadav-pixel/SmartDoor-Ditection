import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

public class FaceDetect{

    static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
}


    public static void main(String[] args) {

        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("Camera not detected");
            return;
        }

        CascadeClassifier faceDetector =
                new CascadeClassifier("haarcascade_frontalface_default.xml");

        Mat frame = new Mat();
        Mat gray = new Mat();

        while (true) {
            camera.read(frame);
            if (frame.empty()) break;

            // Convert to grayscale
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.equalizeHist(gray, gray);

            // Detect faces
            MatOfRect faces = new MatOfRect();
            faceDetector.detectMultiScale(gray, faces);

            // Draw rectangle
            for (Rect rect : faces.toArray()) {
                Imgproc.rectangle(
                        frame,
                        new Point(rect.x, rect.y),
                        new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(0, 255, 0),
                        2
                );
            }

            HighGui.imshow("Face Detection", frame);
            if (HighGui.waitKey(1) == 27) break; // ESC
        }

        camera.release();
        HighGui.destroyAllWindows();
    }
}
