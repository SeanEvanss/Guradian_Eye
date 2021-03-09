#Based on Zed code - Person Fall detection using raspberry pi camera and opencv lib. Link: https://www.youtube.com/watch?v=eXMYZedp0Uo

import cv2
import time
import sys

fitToEllipse = False
capture = cv2.VideoCapture(0)
time.sleep(2)

fall_counter=0

background_model= sys.argv[1]

if(background_model=="KNN"):
    fgbg = cv2.createBackgroundSubtractorKNN()
else:
    fgbg = cv2.createBackgroundSubtractorMOG2()
j = 0

while(1):
    ret, frame = capture.read()
    
    #Convert each frame to gray scale and subtract the background
    try:
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        fgmask = fgbg.apply(gray,frame)
        
        #Find contours
        contours, _ = cv2.findContours(fgmask, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

        if contours:
            # List to hold all areas
            areas = []

            for contour in contours:
                ar = cv2.contourArea(contour)
                areas.append(ar)
            
            max_area = max(areas, default = 0)

            max_area_index = areas.index(max_area)

            cnt = contours[max_area_index]

            M = cv2.moments(cnt)
            
            x, y, w, h = cv2.boundingRect(cnt)

            cv2.drawContours(fgmask, [cnt], 0, (255,255,255), 3, maxLevel = 0)
            
            if h < w:
                j += 1
                
            if j > 10:
                #print("FALL")
                #cv2.putText(fgmask, 'FALL', (x, y), cv2.FONT_HERSHEY_TRIPLEX, 0.5, (255,255,255), 2)
                cv2.rectangle(frame,(x,y),(x+w,y+h),(0,0,255),2)
                print(fall_counter)
                fall_counter+=1

            if h > w:
                j = 0 
                cv2.rectangle(frame,(x,y),(x+w,y+h),(0,255,0),2)


            cv2.imshow('video', frame)
            cv2.imshow('mask',fgmask)
        
            if cv2.waitKey(33) == 27:
             break
    except Exception as e:
        break
cv2.destroyAllWindows()
