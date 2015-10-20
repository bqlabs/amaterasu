import cv2
import numpy as np
import time

__author__ = 'def'

left_camera_device = '/dev/video1'
right_camera_device = '/dev/video2'

left_camera_inverted = False
right_camera_inverted = True

webcam_resolution = (640, 480)
viewer_resolution = (1920, 1080)

def capture_stereo_pair(left_camera, right_camera):
    l_status = left_camera.grab()
    r_status = right_camera.grab()

    if not l_status:
        print "Left camera error (grabbing)"
    if not r_status:
        print "Right camera error (grabbing)"


    l_status, left_picture = left_camera.retrieve()
    r_status, right_picture = right_camera.retrieve()

    if not l_status:
        print "Left camera error (retrieve)"
    if not r_status:
        print "Right camera error (retrieve)"

    return left_picture, right_picture

def capture_stereo_pair_without_sync(left_camera, right_camera):
    l_status, left_picture = left_camera.read()
    r_status, right_picture = right_camera.read()

    if not l_status:
        print "Left camera error (read)"
    if not r_status:
        print "Right camera error (read)"

    return left_picture, right_picture

def main():
    left_camera = cv2.VideoCapture(1)
    left_camera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH, 1024)
    left_camera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT, 768)
    # left_camera.open()
    right_camera = cv2.VideoCapture(2)
    right_camera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH, 1024)
    right_camera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT, 768)
    # right_camera.open()

    if not left_camera.isOpened():
        print "Error opening left camera"
        exit(-1)
    if not right_camera.isOpened():
        print "Error opening right camera"
        exit(-1)

    #  Camera startup
    tries = 0
    while (tries < 10):
        l_status, dummy = left_camera.read()
        r_status, dummy = right_camera.read()

        if l_status and r_status:
            break
        else:
            tries += 1
    else:
        print "Timeout reading cameras"

    left_picture, right_picture = capture_stereo_pair(left_camera, right_camera)
    rotated_right = np.rot90(right_picture, 2)

    cv2.imshow('Left image', left_picture)
    cv2.imshow('Right image', rotated_right)

    # Scale images
    factor = viewer_resolution[1] / float(webcam_resolution[1])
    left_resized = cv2.resize(left_picture, None, fx=factor, fy=factor, interpolation=cv2.INTER_CUBIC)
    right_resized = cv2.resize(rotated_right, None, fx=factor, fy=factor, interpolation=cv2.INTER_CUBIC)

    # Calculate optic axis (Approximately)
    left_center = (left_resized.shape[0]/2, left_resized.shape[1]/2)
    right_center = (right_resized.shape[0]/2, right_resized.shape[1]/2)

    # Concatenate images (based on optical center):
    half_size_eye = viewer_resolution[0]/4
    combined_image = np.concatenate((left_picture[:,left_center[0]-half_size_eye:left_center[0]+half_size_eye, :],
                                     rotated_right[:, right_center[0]-half_size_eye:right_center[0]+half_size_eye, :]),
                                    axis=1)


    cv2.imshow('Stereo pair', combined_image)
    cv2.waitKey(-1)
    cv2.destroyAllWindows()

    cv2.imwrite(time.strftime("stereo-%d_%m_%y-%H_%M_%S.png"), combined_image)

    left_camera.release()
    right_camera.release()

if __name__ == '__main__':
    main()