import cv2
import numpy as np

__author__ = 'def'

left_camera_device = '/dev/video1'
right_camera_device = '/dev/video2'

left_camera_inverted = False
right_camera_inverted = True

webcam_resolution = (640, 480)
viewer_resolution = (1920, 180)

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
    # left_camera.open()
    right_camera = cv2.VideoCapture(2)
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

    left_picture, right_picture = capture_stereo_pair_without_sync(left_camera, right_camera)

    combined_image = np.zeros((webcam_resolution[1], webcam_resolution[0]*2, 3))
    combined_image[:, :640, :] = left_picture
    combined_image[:, 640:, :] = right_picture

    cv2.imshow('Left image', left_picture)
    cv2.imshow('Right image', right_picture)
    cv2.imshow('Stereo pair', combined_image)
    cv2.waitKey(-1)
    cv2.destroyAllWindows()

    left_camera.release()
    right_camera.release()

if __name__ == '__main__':
    main()